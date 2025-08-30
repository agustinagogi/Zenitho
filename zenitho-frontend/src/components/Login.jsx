import React, { useState } from 'react';

const Login = ({ onLoginSuccess }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`${import.meta.env.VITE_API_URL}/api/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password }),
            });

            if (response.ok) {
                const token = await response.text();
                localStorage.setItem('jwtToken', token);
                console.log('Login exitoso. Token JWT guardado.');
                onLoginSuccess(); // 👈 Llama a la función del padre para actualizar el estado
            } else {
                console.error('Credenciales incorrectas.');
            }
        } catch (error) {
            console.error('Hubo un problema con la solicitud:', error);
        }
    };

    return (
        <form onSubmit={handleLogin} className="p-4 flex flex-col space-y-4">
            <h2 className="text-2xl font-bold">Iniciar Sesión</h2>
            <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Email"
                className="p-2 border border-gray-300 rounded"
                required
            />
            <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Contraseña"
                className="p-2 border border-gray-300 rounded"
                required
            />
            <button type="submit" className="p-2 bg-green-500 text-white rounded hover:bg-green-600">
                Iniciar Sesión
            </button>
        </form>
    );
};

export default Login;