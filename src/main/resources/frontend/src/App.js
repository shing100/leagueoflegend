import React, {useState, useEffect} from 'react';

function App() {
    const [message, setMessage] = useState("");
    useEffect(() => {
        fetch('/summoner/연구바이블')
            .then(response => response.text())
            .then(message => {
                setMessage(message);
            });
    },[])

    return (
        <div className="App">
            <header className="App-header">
                <h1 className="App-title">{message}</h1>
            </header>
        </div>
    );
}

export default App;
