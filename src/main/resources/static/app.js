const {useEffect, useMemo, useRef, useState} = React;

function App() {
    const [view, setView] = useState("chat");
    const [messages, setMessages] = useState([]);
    const [question, setQuestion] = useState("");
    const [recommendedQuestions, setRecommendedQuestions] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [progressMessage, setProgressMessage] = useState("");
    const [error, setError] = useState("");
    const [isLoginOpen, setIsLoginOpen] = useState(false);
    const [adminToken, setAdminToken] = useState(() => localStorage.getItem("adminToken") || "");
    const [isCheckingAdmin, setIsCheckingAdmin] = useState(false);
    const scrollerRef = useRef(null);
    const messageListRef = useRef(null);

    const hasMessages = messages.length > 0;
    const isAdmin = Boolean(adminToken);
    const adminLabel = useMemo(() => isAdmin ? "관리자 페이지" : "관리자 로그인", [isAdmin]);

    useEffect(() => {
        fetchRecommendedQuestions();
        fetch("/api/myinfo/visits", {method: "POST"}).catch(() => {});
    }, []);

    useEffect(() => {
        const list = messageListRef.current;
        if (list) {
            list.scrollTop = list.scrollHeight;
        }
    }, [messages, isLoading]);

    async function fetchRecommendedQuestions() {
        try {
            const response = await fetch("/api/myinfo/recommended-questions");
            if (!response.ok) {
                throw new Error("추천 질문을 불러오지 못했습니다.");
            }
            const data = await response.json();
            setRecommendedQuestions(Array.isArray(data) ? data : []);
        } catch (error) {
            setError(error.message);
        }
    }

    async function submitQuestion(nextQuestion = question) {
        const trimmedQuestion = nextQuestion.trim();
        if (!trimmedQuestion || isLoading) {
            return;
        }

        setError("");
        setQuestion("");
        setMessages((prev) => [...prev, {role: "user", content: trimmedQuestion}]);
        setIsLoading(true);
        setProgressMessage("질문에 맞는 문서리스트 선별중..");

        try {
            const response = await fetch("/api/ask/progress", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({question: trimmedQuestion})
            });

            if (!response.ok) {
                throw new Error("답변 생성에 실패했습니다.");
            }

            const data = await readProgressResponse(response, setProgressMessage);
            setMessages((prev) => [...prev, {role: "assistant", content: data.answer || "답변이 비어 있습니다."}]);
        } catch (error) {
            setError(error.message);
            setMessages((prev) => [...prev, {role: "assistant", content: "지금은 답변을 생성하지 못했습니다."}]);
        } finally {
            setIsLoading(false);
            setProgressMessage("");
        }
    }

    async function selectRecommendedQuestion(item) {
        if (isLoading) {
            return;
        }

        setError("");
        setMessages((prev) => [...prev, {role: "user", content: item.question}]);
        setIsLoading(true);
        setProgressMessage("저장된 답변을 불러오는 중..");

        try {
            const response = await fetch(`/api/myinfo/recommended-questions/${item.id}/answer`);
            if (!response.ok) {
                throw new Error("추천 질문 답변을 불러오지 못했습니다.");
            }
            const data = await response.json();
            setMessages((prev) => [...prev, {role: "assistant", content: data.answer || "저장된 답변이 비어 있습니다."}]);
        } catch (error) {
            setError(error.message);
            setMessages((prev) => [...prev, {role: "assistant", content: "저장된 답변을 찾지 못했습니다."}]);
        } finally {
            setIsLoading(false);
            setProgressMessage("");
        }
    }

    function slideSuggestions(direction) {
        const scroller = scrollerRef.current;
        if (scroller) {
            scroller.scrollBy({left: direction * 320, behavior: "smooth"});
        }
    }

    function handleKeyDown(event) {
        if (event.key === "Enter" && !event.shiftKey) {
            event.preventDefault();
            submitQuestion();
        }
    }

    function clearAdminSession() {
        localStorage.removeItem("adminToken");
        setAdminToken("");
        setView("chat");
    }

    async function handleAdminButton() {
        if (!isAdmin) {
            setIsLoginOpen(true);
            return;
        }

        setIsCheckingAdmin(true);
        try {
            const response = await fetch("/api/admin/dashboard", {
                headers: {
                    "Authorization": `Bearer ${adminToken}`
                }
            });

            if (response.status === 401 || response.status === 403) {
                clearAdminSession();
                setIsLoginOpen(true);
                return;
            }

            if (!response.ok) {
                throw new Error(`Admin token check failed (${response.status})`);
            }

            setView("admin");
        } catch (error) {
            setError(error.message);
        } finally {
            setIsCheckingAdmin(false);
        }
    }

    function handleLogout() {
        clearAdminSession();
    }

    function handleAdminUnauthorized() {
        clearAdminSession();
        setIsLoginOpen(true);
    }

    return (
        <main className="app-shell">
            <section className="chat-layout">
                <header className="top-bar">
                    <button className="brand brand-button" type="button" onClick={() => setView("chat")} aria-label="채팅 화면으로 이동">
                        <div className="brand-mark">전</div>
                        <div>
                            <div className="brand-title">전남준 AI</div>
                            <div className="brand-subtitle">포트폴리오 문서 기반 질의응답</div>
                        </div>
                    </button>
                    <button className="admin-button" type="button" onClick={handleAdminButton} disabled={isCheckingAdmin}>
                        {adminLabel}
                    </button>
                </header>

                {view === "admin" && isAdmin ? (
                    <AdminPage
                        token={adminToken}
                        onBack={() => setView("chat")}
                        onLogout={handleLogout}
                        onUnauthorized={handleAdminUnauthorized}
                        onRecommendedChanged={fetchRecommendedQuestions}
                    />
                ) : (
                    <>
                        <section className="chat-box" aria-label="대화 영역">
                            <div className="message-list" ref={messageListRef}>
                                {!hasMessages && (
                                    <div className="empty-state">
                                        <div>
                                            <h1 className="empty-title">무엇이 궁금하신가요?</h1>
                                            <p className="empty-copy">
                                                전남준의 프로젝트, 기술스택, 개발 경험에 대해 질문해 주세요.
                                            </p>
                                        </div>
                                    </div>
                                )}

                                {messages.map((message, index) => (
                                    <ChatMessage key={`${message.role}-${index}`} message={message}/>
                                ))}

                                {isLoading && (
                                    <div className="message-row assistant">
                                        <div className="speaker">전남준</div>
                                        <div className="bubble">
                                            <div className="loading-bubble" aria-label="답변 생성중">
                                                <span></span>
                                                <span></span>
                                                <span></span>
                                            </div>
                                            {progressMessage && <div className="loading-status">{progressMessage}</div>}
                                        </div>
                                    </div>
                                )}
                            </div>

                            <SuggestionDock
                                recommendedQuestions={recommendedQuestions}
                                scrollerRef={scrollerRef}
                                onSlide={slideSuggestions}
                                onSelect={selectRecommendedQuestion}
                                error={error}
                            />
                        </section>

                        <form className="composer" onSubmit={(event) => {
                            event.preventDefault();
                            submitQuestion();
                        }}>
                            <textarea
                                value={question}
                                onChange={(event) => setQuestion(event.target.value)}
                                onKeyDown={handleKeyDown}
                                placeholder="전남준에게 질문하기"
                                rows="1"
                            />
                            <button className="send-button" type="submit" disabled={isLoading || !question.trim()} aria-label="질문 보내기">
                                ↑
                            </button>
                        </form>

                        <PortfolioSection/>
                    </>
                )}
            </section>

            {isLoginOpen && (
                <AdminLoginModal
                    onClose={() => setIsLoginOpen(false)}
                    onLogin={(token) => {
                        setAdminToken(token);
                        localStorage.setItem("adminToken", token);
                        setIsLoginOpen(false);
                        setView("admin");
                    }}
                />
            )}
        </main>
    );
}

function PortfolioSection() {
    return (
        <section className="portfolio-section" aria-label="포트폴리오">
            <div className="portfolio-header">
                <h2>Portfolio</h2>
            </div>
            <div className="portfolio-grid">
                <a
                    className="portfolio-card"
                    href="/myinfo/subpofol/MainPofol_portfolio.html"
                    target="_blank"
                    rel="noreferrer"
                >
                    <img
                        src="/myinfo/subpofol/MainPofol_Thumb.png"
                        alt={"MainPofol AI architecture thumbnail"}
                    />
                    <div className="portfolio-card-body">
                        <div className="portfolio-card-title">{"MainPofol (해당 페이지)"}</div>
                    </div>
                </a>
                <a
                    className="portfolio-card"
                    href="/myinfo/subpofol/AllyaZoom_portpolio.html"
                    target="_blank"
                    rel="noreferrer"
                >
                    <img
                        src="/myinfo/subpofol/AllyaZoom_Thumb.png"
                        alt={"AllyaZoom\uC11C\uBE44\uC2A4 thumbnail"}
                    />
                    <div className="portfolio-card-body">
                        <div className="portfolio-card-title">{"AllyaZoom\uC11C\uBE44\uC2A4"}</div>
                    </div>
                </a>
            </div>
        </section>
    );
}

async function readProgressResponse(response, onProgress) {
    const reader = response.body.getReader();
    const decoder = new TextDecoder("utf-8");
    let buffer = "";
    let result = null;

    while (true) {
        const {value, done} = await reader.read();
        if (done) {
            break;
        }

        buffer += decoder.decode(value, {stream: true});
        const lines = buffer.split("\n");
        buffer = lines.pop();

        for (const line of lines) {
            if (!line.trim()) {
                continue;
            }

            const event = JSON.parse(line);
            if (event.type === "progress") {
                onProgress(event.message);
            }
            if (event.type === "done") {
                result = event.data;
            }
        }
    }

    if (buffer.trim()) {
        const event = JSON.parse(buffer);
        if (event.type === "done") {
            result = event.data;
        }
    }

    if (!result) {
        throw new Error("답변 생성 결과를 받지 못했습니다.");
    }

    return result;
}

function ChatMessage({message}) {
    const speaker = message.role === "user" ? "당신" : "전남준";
    return (
        <div className={`message-row ${message.role}`}>
            <div className="speaker">{speaker}</div>
            <div className="bubble">{message.content}</div>
        </div>
    );
}

function SuggestionDock({recommendedQuestions, scrollerRef, onSlide, onSelect, error}) {
    return (
        <aside className="suggestion-dock" aria-label="추천 질문">
            <div className="suggestion-header">
                <div className="suggestion-title">추천 질문</div>
                <div className="slider-controls">
                    <button className="slider-button" type="button" onClick={() => onSlide(-1)} aria-label="추천 질문 왼쪽으로 이동">
                        ‹
                    </button>
                    <button className="slider-button" type="button" onClick={() => onSlide(1)} aria-label="추천 질문 오른쪽으로 이동">
                        ›
                    </button>
                </div>
            </div>

            {error && <div className="error-text">{error}</div>}

            <div className="suggestion-scroller" ref={scrollerRef}>
                {recommendedQuestions.length === 0 && (
                    <div className="suggestion-empty">등록된 추천 질문이 없습니다.</div>
                )}
                {recommendedQuestions.map((item) => (
                    <button
                        className="suggestion-card"
                        key={item.id}
                        type="button"
                        onClick={() => onSelect(item)}
                    >
                        {item.question}
                    </button>
                ))}
            </div>
        </aside>
    );
}

function AdminPage({token, onBack, onLogout, onUnauthorized, onRecommendedChanged}) {
    const [dashboard, setDashboard] = useState({visitCount: 0});
    const [histories, setHistories] = useState([]);
    const [recommendedQuestions, setRecommendedQuestions] = useState([]);
    const [documents, setDocuments] = useState([]);
    const [activeTab, setActiveTab] = useState("histories");
    const [error, setError] = useState("");

    useEffect(() => {
        refreshAll();
    }, []);

    async function adminFetch(url, options = {}) {
        const response = await fetch(url, {
            ...options,
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
                ...(options.headers || {})
            }
        });
        if (response.status === 401 || response.status === 403) {
            onUnauthorized();
            throw new Error("Admin authorization expired.");
        }
        if (!response.ok) {
            throw new Error(`요청 실패 (${response.status})`);
        }
        return response.status === 204 ? null : response.json();
    }

    async function refreshAll() {
        setError("");
        try {
            const [dashboardData, historyPage, recommendedPage, documentList] = await Promise.all([
                adminFetch("/api/admin/dashboard"),
                adminFetch("/api/admin/myinfo-ask-histories?size=50&sort=id,desc"),
                adminFetch("/api/admin/recommended-questions?size=50&sort=displayOrder,asc&sort=id,asc"),
                adminFetch("/api/admin/myinfo-documents")
            ]);
            setDashboard(dashboardData);
            setHistories(historyPage.content || []);
            setRecommendedQuestions(recommendedPage.content || []);
            setDocuments(documentList || []);
        } catch (error) {
            setError(error.message);
        }
    }

    async function refreshDocuments() {
        const documentList = await adminFetch("/api/admin/myinfo-documents");
        setDocuments(documentList || []);
    }

    async function deleteHistory(id) {
        if (!confirm("질문 히스토리를 삭제할까요?")) {
            return;
        }
        try {
            await adminFetch(`/api/admin/myinfo-ask-histories/${id}`, {method: "DELETE"});
            await refreshAll();
        } catch (error) {
            setError(error.message);
        }
    }

    async function deleteRecommended(id) {
        if (!confirm("추천 질문을 삭제할까요?")) {
            return;
        }
        try {
            await adminFetch(`/api/admin/recommended-questions/${id}`, {method: "DELETE"});
            await refreshAll();
            onRecommendedChanged();
        } catch (error) {
            setError(error.message);
        }
    }

    async function registerRecommended(history) {
        try {
            await adminFetch(`/api/admin/myinfo-ask-histories/${history.id}/recommended-question`, {
                method: "POST",
                body: JSON.stringify({
                    question: history.question,
                    enabled: true,
                    displayOrder: recommendedQuestions.length
                })
            });
            await refreshAll();
            onRecommendedChanged();
            setActiveTab("recommended");
        } catch (error) {
            setError(error.message);
        }
    }

    async function toggleRecommended(item) {
        try {
            await adminFetch(`/api/admin/recommended-questions/${item.id}`, {
                method: "PUT",
                body: JSON.stringify({
                    question: item.question,
                    askHistoryId: item.askHistoryId,
                    enabled: !item.enabled,
                    displayOrder: item.displayOrder
                })
            });
            await refreshAll();
            onRecommendedChanged();
        } catch (error) {
            setError(error.message);
        }
    }

    return (
        <section className="admin-page">
            <div className="admin-page-header">
                <div>
                    <h1>관리자 페이지</h1>
                    <p>방문자수, 질문 히스토리, 추천 질문을 관리합니다.</p>
                </div>
                <div className="admin-page-actions">
                    <button type="button" onClick={onBack}>채팅으로</button>
                    <button type="button" onClick={onLogout}>로그아웃</button>
                </div>
            </div>

            <div className="metric-grid">
                <div className="metric-card">
                    <div className="metric-label">방문자수</div>
                    <div className="metric-value">{dashboard.visitCount?.toLocaleString?.() || 0}</div>
                </div>
                <div className="metric-card">
                    <div className="metric-label">질문 히스토리</div>
                    <div className="metric-value">{histories.length}</div>
                </div>
                <div className="metric-card">
                    <div className="metric-label">추천 질문</div>
                    <div className="metric-value">{recommendedQuestions.length}</div>
                </div>
                <div className="metric-card">
                    <div className="metric-label">문서</div>
                    <div className="metric-value">{documents.length}</div>
                </div>
            </div>

            <div className="chart-grid">
                <VisitBarChart title="일자별 방문자수" items={dashboard.dailyVisits || []}/>
                <VisitBarChart title="오늘 시간대별 방문자수" items={normalizeHourlyVisits(dashboard.hourlyVisits || [])}/>
            </div>

            {error && <div className="admin-error">{error}</div>}

            <div className="admin-tabs">
                <button className={activeTab === "histories" ? "active" : ""} type="button" onClick={() => setActiveTab("histories")}>
                    질문 히스토리 관리
                </button>
                <button className={activeTab === "recommended" ? "active" : ""} type="button" onClick={() => setActiveTab("recommended")}>
                    추천 질문 DB 관리
                </button>
                <button className={activeTab === "documents" ? "active" : ""} type="button" onClick={() => setActiveTab("documents")}>
                    문서 관리
                </button>
                <button type="button" onClick={refreshAll}>새로고침</button>
            </div>

            {activeTab === "histories" && (
                <HistoryTable histories={histories} onDelete={deleteHistory} onRegister={registerRecommended}/>
            )}
            {activeTab === "recommended" && (
                <RecommendedTable items={recommendedQuestions} onDelete={deleteRecommended} onToggle={toggleRecommended}/>
            )}
            {activeTab === "documents" && (
                <DocumentManager documents={documents} adminFetch={adminFetch} onChanged={refreshDocuments}/>
            )}
        </section>
    );
}

function VisitBarChart({title, items}) {
    const maxCount = Math.max(1, ...items.map((item) => item.count || 0));

    return (
        <section className="visit-chart">
            <div className="visit-chart-title">{title}</div>
            <div className="visit-chart-bars">
                {items.length === 0 && <div className="suggestion-empty">방문 기록이 없습니다.</div>}
                {items.map((item) => (
                    <div className="visit-bar-item" key={item.label}>
                        <div className="visit-bar-track">
                            <div
                                className="visit-bar-fill"
                                style={{height: `${Math.max(4, Math.round(((item.count || 0) / maxCount) * 100))}%`}}
                                title={`${item.label}: ${item.count}`}
                            />
                        </div>
                        <div className="visit-bar-count">{item.count}</div>
                        <div className="visit-bar-label">{item.label}</div>
                    </div>
                ))}
            </div>
        </section>
    );
}

function normalizeHourlyVisits(items) {
    const countMap = new Map(items.map((item) => [item.label, item.count]));
    return Array.from({length: 24}, (_, hour) => {
        const label = `${String(hour).padStart(2, "0")}시`;
        return {
            label,
            count: countMap.get(label) || 0
        };
    });
}

function HistoryTable({histories, onDelete, onRegister}) {
    const [expandedAnswerIds, setExpandedAnswerIds] = useState([]);

    function isExpanded(id) {
        return expandedAnswerIds.includes(id);
    }

    function toggleAnswer(id) {
        setExpandedAnswerIds((prev) => (
            prev.includes(id)
                ? prev.filter((answerId) => answerId !== id)
                : [...prev, id]
        ));
    }

    return (
        <div className="admin-table-wrap">
            <table className="admin-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>질문</th>
                    <th>답변</th>
                    <th>생성시간</th>
                    <th>관리</th>
                </tr>
                </thead>
                <tbody>
                {histories.length === 0 && (
                    <tr>
                        <td colSpan="5">질문 히스토리가 없습니다.</td>
                    </tr>
                )}
                {histories.map((history) => (
                    <tr key={history.id}>
                        <td>{history.id}</td>
                        <td>{history.question}</td>
                        <td>
                            <div className={isExpanded(history.id) ? "answer-cell expanded" : "answer-cell"}>
                                {history.answer}
                            </div>
                            <button className="answer-toggle" type="button" onClick={() => toggleAnswer(history.id)}>
                                {isExpanded(history.id) ? "접기" : "펼치기"}
                            </button>
                        </td>
                        <td>{formatDateTime(history.createdAt)}</td>
                        <td>
                            <div className="table-actions">
                                <button type="button" onClick={() => onRegister(history)}>추천 등록</button>
                                <button type="button" onClick={() => onDelete(history.id)}>삭제</button>
                            </div>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

function RecommendedTable({items, onDelete, onToggle}) {
    return (
        <div className="admin-table-wrap">
            <table className="admin-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>질문</th>
                    <th>히스토리 ID</th>
                    <th>노출</th>
                    <th>정렬</th>
                    <th>관리</th>
                </tr>
                </thead>
                <tbody>
                {items.length === 0 && (
                    <tr>
                        <td colSpan="6">추천 질문이 없습니다.</td>
                    </tr>
                )}
                {items.map((item) => (
                    <tr key={item.id}>
                        <td>{item.id}</td>
                        <td>{item.question}</td>
                        <td>{item.askHistoryId}</td>
                        <td>{item.enabled ? "ON" : "OFF"}</td>
                        <td>{item.displayOrder}</td>
                        <td>
                            <div className="table-actions">
                                <button type="button" onClick={() => onToggle(item)}>
                                    {item.enabled ? "숨김" : "노출"}
                                </button>
                                <button type="button" onClick={() => onDelete(item.id)}>삭제</button>
                            </div>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

function DocumentManager({documents, adminFetch, onChanged}) {
    const [selectedFileName, setSelectedFileName] = useState("");
    const [fileName, setFileName] = useState("");
    const [content, setContent] = useState("");
    const [isNew, setIsNew] = useState(false);
    const [message, setMessage] = useState("");

    async function loadDocument(nextFileName) {
        setMessage("");
        const document = await adminFetch(`/api/admin/myinfo-documents/${encodeURIComponent(nextFileName)}`);
        setSelectedFileName(document.fileName);
        setFileName(document.fileName);
        setContent(document.content || "");
        setIsNew(false);
    }

    function startCreate() {
        setSelectedFileName("");
        setFileName("");
        setContent("");
        setIsNew(true);
        setMessage("");
    }

    async function saveDocument(event) {
        event.preventDefault();
        setMessage("");

        if (isNew) {
            await adminFetch("/api/admin/myinfo-documents", {
                method: "POST",
                body: JSON.stringify({fileName, content})
            });
            setSelectedFileName(fileName);
            setIsNew(false);
        } else {
            await adminFetch(`/api/admin/myinfo-documents/${encodeURIComponent(selectedFileName)}`, {
                method: "PUT",
                body: JSON.stringify({content})
            });
        }

        await onChanged();
        setMessage("저장했습니다.");
    }

    async function deleteDocument() {
        if (!selectedFileName || !confirm(`${selectedFileName} 문서를 삭제할까요?`)) {
            return;
        }

        await adminFetch(`/api/admin/myinfo-documents/${encodeURIComponent(selectedFileName)}`, {
            method: "DELETE"
        });
        setSelectedFileName("");
        setFileName("");
        setContent("");
        setIsNew(false);
        await onChanged();
        setMessage("삭제했습니다.");
    }

    return (
        <div className="document-manager">
            <div className="document-list-panel">
                <div className="document-toolbar">
                    <div className="document-title">myinfo 문서</div>
                    <button type="button" onClick={startCreate}>새 문서</button>
                </div>
                <div className="document-list">
                    {documents.length === 0 && <div className="suggestion-empty">문서가 없습니다.</div>}
                    {documents.map((document) => (
                        <button
                            className={selectedFileName === document.fileName ? "document-list-item active" : "document-list-item"}
                            key={document.fileName}
                            type="button"
                            onClick={() => loadDocument(document.fileName)}
                        >
                            <span>{document.fileName}</span>
                            <small>{document.size.toLocaleString()} bytes</small>
                        </button>
                    ))}
                </div>
            </div>

            <form className="document-editor" onSubmit={saveDocument}>
                <label className="modal-field">
                    파일명
                    <input
                        value={fileName}
                        onChange={(event) => setFileName(event.target.value)}
                        placeholder="example.md"
                        disabled={!isNew}
                    />
                </label>
                <label className="modal-field document-content-field">
                    <span>내용</span>
                    <textarea
                        value={content}
                        onChange={(event) => setContent(event.target.value)}
                        placeholder="Markdown 문서 내용을 입력하세요."
                    />
                </label>
                {message && <div className="document-message">{message}</div>}
                <div className="document-actions">
                    <button type="submit" disabled={!fileName.trim() || !content.trim()}>
                        저장
                    </button>
                    {!isNew && selectedFileName && selectedFileName !== "checklist.md" && (
                        <button type="button" onClick={deleteDocument}>삭제</button>
                    )}
                </div>
            </form>
        </div>
    );
}

function AdminLoginModal({onClose, onLogin}) {
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [isSubmitting, setIsSubmitting] = useState(false);

    async function submit(event) {
        event.preventDefault();
        if (!password.trim() || isSubmitting) {
            return;
        }

        setError("");
        setIsSubmitting(true);

        try {
            const response = await fetch("/api/admin/login", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({password})
            });
            if (!response.ok) {
                throw new Error("관리자 로그인에 실패했습니다.");
            }
            const data = await response.json();
            onLogin(data.accessToken);
        } catch (error) {
            setError(error.message);
        } finally {
            setIsSubmitting(false);
        }
    }

    return (
        <div className="modal-backdrop" role="presentation" onMouseDown={onClose}>
            <form className="modal" onSubmit={submit} onMouseDown={(event) => event.stopPropagation()}>
                <h2>관리자 로그인</h2>
                <label className="modal-field">
                    비밀번호
                    <input
                        type="password"
                        value={password}
                        onChange={(event) => setPassword(event.target.value)}
                        placeholder="관리자 비밀번호"
                        autoFocus
                    />
                </label>
                {error && <p className="error-text">{error}</p>}
                <div className="modal-actions">
                    <button type="button" onClick={onClose}>닫기</button>
                    <button className="primary" type="submit" disabled={isSubmitting}>
                        로그인
                    </button>
                </div>
            </form>
        </div>
    );
}

function formatDateTime(value) {
    if (!value) {
        return "-";
    }
    return String(value).replace("T", " ").slice(0, 19);
}

ReactDOM.createRoot(document.getElementById("root")).render(<App/>);
