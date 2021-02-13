import React from 'react';
import styled, { ThemeProvider } from "styled-components";
import { HashRouter as Router } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import GlobalStyles from '../../Styles/GlobalStyles';
import AppRouter from '../Routes';
import Theme from '../../Styles/Theme';
import Footer from "../Footer";
import Header from "../Header";
import {StyleReset} from "atomize";

const Wrapper = styled.div `
    margin: 0 auto;
    max-width: ${props => props.theme.maxWidth};
    width: 100%;
`;

const AppContainer = () => {
    return (
        <ThemeProvider theme={Theme}>
            <>
                <StyleReset />
                <GlobalStyles />
                <Router>
                    <>
                        <Header />
                        <Wrapper>
                            <AppRouter isLoggedIn={true} />
                            <Footer />
                        </Wrapper>
                    </>
                </Router>
                <ToastContainer position={toast.POSITION.BOTTOM_LEFT} />
            </>
        </ThemeProvider>
    )
}

export default AppContainer;