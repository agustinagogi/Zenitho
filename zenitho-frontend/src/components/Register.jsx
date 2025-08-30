import React, {useState} from 'react';

const Register = () => {
    const [formData, setFormData] = useState({
        name: '',
        username: '',
        email: '',
        password: ''
    })

    const handleChange = (e) => {
        // e.target es el elemento del DOM (el campo de entrada en este caso)
        // e.target.name devuelve el valor del atributo name del campo que ha cambiado (por ejemplo "username" o "password"
        // e.target.value devuelve el valor actual que se ha escrito en ese campo
        setFormData({ ...formData, [e.target.name]: e.target.value })
    }

    // Función asíncrona que se activa cuando se envía un formulario
    // Gestiona el envío de datos del formulario a un servidor sin recargar la página
    const handleSubmit = async (e) => {
        e.preventDefault() // Evita el comportamiento predeterminado del formulario en el navegador, que es recargar la página
        // Si no lo incluimos, la página se recargaría y la solicitud fetch no se completaría

        try {
            const response = await fetch(`${import.meta.env.VITE_API_URL}/api/users`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(formData)
            });

            if(response.ok){
                console.log("Successfully registered");
            } else {
                console.error("Error occured registering user");
            }
        } catch (error) {
            console.error("There was a problem in request: ", error);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="p-4 flex flex-col space-y-4">
            <h2 className="text-2xl font-bold">Register</h2>
            <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                placeholder="Nombre"
                className="p-2 border border-gray-300 rounded"
                required
            />
            <input
                type="text"
                name="username"
                value={formData.username}
                onChange={handleChange}
                placeholder="Nombre de usuario"
                className="p-2 border border-gray-300 rounded"
                required
                />
            <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder="Email"
                className="p-2 border border-gray-300 rounded"
                required
                />
            <input
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Password"
                className="p-2 border border-gray-300 rounded"
                required
            />
            <button type="submit" className="p-2 bg-blue-500 text-white rounded hover:bg-blue-600">Register</button>
        </form>
    );
};

export default Register;
