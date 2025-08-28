import React, { useState, useEffect } from 'react';
import Login from './components/Login.jsx';
import Dashboard from './components/Dashboard.jsx';
import './App.css';
import Register from './components/Register.jsx';

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [isRegistering, setIsRegistering] = useState(false); // Nuevo estado

    useEffect(() => {
        const token = localStorage.getItem('jwtToken');
        if (token) {
            setIsLoggedIn(true);
        }
    }, []);

    const handleLoginSuccess = () => {
        setIsLoggedIn(true);
    };

    const handleRegisterSuccess = () => {
        // Lógica después de un registro exitoso (ej. iniciar sesión automáticamente)
        setIsLoggedIn(true);
    };

    const handleLogoutSuccess = () => {
        localStorage.removeItem('jwtToken');
        setIsLoggedIn(false);
    }

    if (isLoggedIn) {
        return <Dashboard onLogout={handleLogoutSuccess} />;
    }

    return (
        <div className="flex justify-center items-center h-screen bg-gray-100">
            <div className="bg-white p-8 rounded-lg shadow-md w-96">
                {isRegistering ? (
                    <>
                        <Register onRegisterSuccess={handleRegisterSuccess} />
                        <p className="mt-4 text-center">
                            ¿Ya tienes una cuenta?{' '}
                            <button onClick={() => setIsRegistering(false)} className="text-blue-500 hover:underline">
                                Inicia sesión
                            </button>
                        </p>
                    </>
                ) : (
                    <>
                        <Login onLoginSuccess={handleLoginSuccess} />
                        <p className="mt-4 text-center">
                            ¿No tienes una cuenta?{' '}
                            <button onClick={() => setIsRegistering(true)} className="text-blue-500 hover:underline">
                                Regístrate
                            </button>
                        </p>
                    </>
                )}
            </div>
        </div>
    );
}

export default App;