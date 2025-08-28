import React, { useState, useEffect } from 'react';
import KanbanBoard from './KanbanBoard';

const Dashboard = () => {
    const [user, setUser] = useState(null);
    // Agregamos un estado para forzar la recarga del tablero
    const [refreshBoards, setRefreshBoards] = useState(false);

    useEffect(() => {
        const fetchUser = async () => {
            const token = localStorage.getItem('jwtToken');
            if (token) {
                // Aquí se podría usar un endpoint del backend para obtener el usuario
                setUser({ name: 'Agustinag' }); // Dato de prueba
            }
        };
        fetchUser();
    }, []);

    const handleAddBoard = async () => {
        const title = window.prompt("Introduce el nombre del nuevo tablero:");
        if (title) {
            const token = localStorage.getItem('jwtToken');
            if (!token) {
                alert('No estás autenticado.');
                return;
            }

            try {
                const response = await fetch('http://localhost:8080/api/boards', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${token}`
                    },
                    // 👈 Envía un objeto JSON con el campo 'title'
                    body: JSON.stringify({ title: title })
                });

                if (response.ok) {
                    alert(`Tablero "${title}" creado con éxito.`);
                    // Forzamos la recarga del componente KanbanBoard
                    setRefreshBoards(prev => !prev);
                } else {
                    const errorData = await response.json();
                    alert(`Error al crear el tablero: ${errorData.message}`);
                }
            } catch (error) {
                alert("Error de conexión al crear el tablero.");
            }
        }
    };

    const handleAddCard = () => {
        console.log("Añadir tarjeta clickeado");
    };

    if (!user) {
        return <div className="p-4">Cargando datos de usuario...</div>;
    }

    return (
        <div className="flex flex-col h-screen">
            {/* Barra superior (Navbar) */}
            <div className="bg-white shadow-md p-4 flex justify-between items-center">
                <h1 className="text-xl font-bold">Zenitho</h1>
                <div className="flex items-center space-x-4">
                    <span className="font-semibold">Bienvenido, {user.name}</span>
                </div>
            </div>

            {/* Contenido principal */}
            <div className="flex flex-1 overflow-hidden">
                {/* Contenido principal: KanbanBoard */}
                <div className="flex-1 overflow-auto">
                    {/* Le pasamos el estado para que se recargue */}
                    <KanbanBoard key={refreshBoards} />
                </div>

                {/* Menú lateral derecho */}
                <div className="w-64 bg-gray-100 p-4 shadow-inner overflow-y-auto flex-shrink-0">
                    <h2 className="text-lg font-bold mb-4">Opciones</h2>
                    <div className="space-y-2">
                        <button
                            onClick={handleAddBoard}
                            className="w-full bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded"
                        >
                            Añadir Tablero
                        </button>
                        <button
                            onClick={handleAddCard}
                            className="w-full bg-green-500 hover:bg-green-600 text-white font-bold py-2 px-4 rounded"
                        >
                            Añadir Tarjeta
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;