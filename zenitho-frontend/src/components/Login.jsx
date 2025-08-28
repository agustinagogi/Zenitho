import React, {useState} from 'react';

const Login = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({email, password}),
            });

            if(response.ok) {
                const token = await response.json();
                localStorage.setItem('jwtToken', token);
                console.log("Successfully logged in");
            } else {
                console.error('Unable to log in.');
            }
        } catch (error){
            console.error('Unable to log in.');
        }
    };

    return (
        <form onSubmit={handleLogin} className="p-4 flex flex-col space-y-4">
            <h2 className="text-2xl font-bold">Iniciar sesión</h2>
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
            <button type="submit" className="p-2 bg-green-500 text-white rounded hover:bg-green-600">Iniciar sesión
            </button>
        </form>
    );
};

export default Login;