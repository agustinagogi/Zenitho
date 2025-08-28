import React, { useState, useEffect } from 'react';
import KanbanBoard from './KanbanBoard';
import AddBoardModal from './AddBoardModal';
import AddCardModal from './AddCardModal';

const Dashboard = () => {
    const [user, setUser] = useState(null);
    const [boardData, setBoardData] = useState(null);
    const [refreshBoards, setRefreshBoards] = useState(false);
    const [showAddBoardModal, setShowAddBoardModal] = useState(false);
    const [showAddCardModal, setShowAddCardModal] = useState(false);

    useEffect(() => {
        const fetchUserData = async () => {
            const token = localStorage.getItem('jwtToken');
            if (token) {
                try {
                    // Llamada al nuevo endpoint para obtener los datos del usuario
                    const userResponse = await fetch('http://localhost:8080/api/users/me', {
                        headers: {
                            'Authorization': `Bearer ${token}`,
                        },
                    });

                    if (userResponse.ok) {
                        const userData = await userResponse.json();
                        setUser(userData); // Guardamos el objeto completo del usuario
                    } else {
                        console.error("Error al obtener los datos del usuario.");
                        return; // Detenemos la ejecución si no podemos obtener el usuario
                    }

                    // Obtenemos los datos del tablero
                    const boardResponse = await fetch('http://localhost:8080/api/boards', {
                        headers: {
                            'Authorization': `Bearer ${token}`,
                        },
                    });

                    if (!boardResponse.ok) {
                        throw new Error('Error al obtener los tableros.');
                    }

                    const boards = await boardResponse.json();
                    if (boards && boards.length > 0) {
                        setBoardData(boards[0]);
                    }
                } catch (error) {
                    console.error("Error al obtener los datos de la aplicación:", error);
                }
            }
        };

        fetchUserData();
    }, [refreshBoards]);

    const handleAddBoard = async (title) => {
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
                body: JSON.stringify({ title: title })
            });

            if (response.ok) {
                alert(`Tablero "${title}" creado con éxito.`);
                setRefreshBoards(prev => !prev);
                setShowAddBoardModal(false);
            } else {
                const errorData = await response.json();
                alert(`Error al crear el tablero: ${errorData.message}`);
            }
        } catch (error) {
            alert("Error de conexión al crear el tablero.");
        }
    };

    const handleAddCard = async (title, content, columnId) => {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            alert('No estás autenticado.');
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/cards?columnId=${columnId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify({ title, content })
            });

            if (response.ok) {
                alert(`Tarjeta "${title}" creada con éxito.`);
                setRefreshBoards(prev => !prev);
                setShowAddCardModal(false);
            } else {
                const errorData = await response.json();
                alert(`Error al crear la tarjeta: ${errorData.message}`);
            }
        } catch (error) {
            alert("Error de conexión al crear la tarjeta.");
        }
    };

    if (!user || !boardData) {
        return <div className="p-4">Cargando datos de usuario y tablero...</div>;
    }

    return (
        <div className="flex h-screen bg-gray-200">
            <div className="w-64 bg-white p-4 shadow-md flex-shrink-0 flex flex-col justify-start items-center space-y-6">
                <h1 className="text-2xl font-bold text-gray-900 mb-8">Zenitho</h1>
                <div className="w-full space-y-2">
                    <button
                        onClick={() => setShowAddBoardModal(true)}
                        className="w-full p-2 bg-blue-500 hover:bg-blue-600 text-white font-bold rounded-md"
                    >
                        Añadir Tablero
                    </button>
                    <button
                        onClick={() => setShowAddCardModal(true)}
                        className="w-full p-2 bg-green-500 hover:bg-green-600 text-white font-bold rounded-md"
                    >
                        Añadir Tarjeta
                    </button>
                </div>
            </div>

            <div className="flex-1 flex flex-col">
                <div className="bg-white shadow-md p-4 flex justify-end items-center">
                    {/* Usamos el nombre real del usuario */}
                    <span className="font-semibold text-gray-900">Bienvenido, {user.name}</span>
                </div>

                <div className="flex-1 overflow-auto p-4">
                    <KanbanBoard board={boardData} key={refreshBoards} />
                </div>
            </div>

            <AddBoardModal
                isOpen={showAddBoardModal}
                onClose={() => setShowAddBoardModal(false)}
                onAddBoard={handleAddBoard}
            />

            <AddCardModal
                isOpen={showAddCardModal}
                onClose={() => setShowAddCardModal(false)}
                onAddCard={handleAddCard}
                columns={boardData.columns || []}
            />
        </div>
    );
};

export default Dashboard;