import React, {useState, useEffect} from 'react';
import { withRouter } from "react-router-dom";
import ExplorePresenter from "./ExplorePresenter";

export default withRouter(({ location: { search } }) => {
    const summoner = search.split("=")[1];
    const [data, setData] = useState("");
    useEffect(() => {
        fetch('/summoner/' + summoner)
            .then(response => response.text())
            .then(data => {
                setData(data);
            });
    },[])

    return <ExplorePresenter summoner={summoner} data={data} />;
});