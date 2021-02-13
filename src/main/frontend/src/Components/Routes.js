import PropTypes from "prop-types";
import React from "react";
import { Route, Switch, Redirect } from "react-router-dom";
import Explore from "../Routes/Explore";
import Search from "../Routes/Explore";

const LoggedInRoutes = () => (
    <Switch>
        <Route exact path="/" component={Explore} />
        <Route path="/explore" component={Explore} />
        <Route path="/search" component={Search} />
        <Route path="/:username" component={Explore} />
        <Redirect from="*" to="/" />
    </Switch>
)

const LoggedOutRoutes = () => (
    <Switch>
        <Route exact path="/" component={Explore} />
        <Redirect from="*" to="/" />
    </Switch>
)

const AppRouter = ({ isLoggedIn }) =>
    isLoggedIn ? <LoggedInRoutes /> : <LoggedOutRoutes />;


// eslint-disable-next-line react/no-typos
AppRouter.propTypes = {
    isLoggedIn: PropTypes.bool.isRequired
};

export default AppRouter;