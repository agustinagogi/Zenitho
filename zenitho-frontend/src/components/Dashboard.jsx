import React, { useState, useEffect } from 'react';
import KanbanBoard from './KanbanBoard';
import AddBoardModal from './AddBoardModal';
import AddCardModal from './AddCardModal';
import AddColumnModal from './AddColumnModal';

const Dashboard = ({onLogout}) => {
    const [user, setUser] = useState(null);
    const [boards, setBoards] = useState([]);
    const [activeBoard, setActiveBoard] = useState(null);
    const [refreshBoards, setRefreshBoards] = useState(false);
    const [showAddBoardModal, setShowAddBoardModal] = useState(false);
    const [showAddCardModal, setShowAddCardModal] = useState(false);
    const [showAddColumnModal, setShowAddColumnModal] = useState(false);

    useEffect(() => {
        const fetchUserData = async () => {
            const token = localStorage.getItem('jwtToken');
            if (!token) {
                // Si no hay token, no intentes cargar datos.
                onLogout();
                return;
            }

            try {
                // Llamada para obtener los datos del usuario
                const userResponse = await fetch('http://localhost:8080/api/users/me', {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

                if (!userResponse.ok) {
                    console.error("Error al obtener los datos del usuario. Forzando cierre de sesión.");
                    onLogout();
                    return;
                }

                const userData = await userResponse.json();
                setUser(userData);

                // Llamada para obtener los datos de los tableros
                const boardResponse = await fetch('http://localhost:8080/api/boards', {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

                if (!boardResponse.ok) {
                    console.error("Error al obtener los tableros. Forzando cierre de sesión.");
                    onLogout();
                    return;
                }

                const boards = await boardResponse.json();
                if (boards && boards.length > 0) {
                    setBoards(boards);
                    if (activeBoard) {
                        const current = boards.find(b => b.id === activeBoard.id) || boards[0];
                        setActiveBoard(current);
                    } else {
                        setActiveBoard(boards[0]);
                    }
                }
            } catch (error) {
                console.error("Error al obtener los datos de la aplicación:", error);
                onLogout();
            }
        };

        fetchUserData();
    }, [refreshBoards, onLogout]);

    const handleLogoutSuccess = () => {
        localStorage.removeItem('jwtToken');
        if (onLogout) {
            onLogout();
        }
    };

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
                const newBoard = await response.json();
                alert(`Tablero "${title}" creado con éxito.`);
                setBoards(prev => [...prev, newBoard]);
                setActiveBoard(newBoard);
                setShowAddBoardModal(false);
            } else {
                const errorData = await response.json();
                alert(`Error al crear el tablero: ${errorData.message}`);
            }
        } catch (error) {
            console.error(error);
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
            console.error(error);
            alert("Error de conexión al crear la tarjeta.");
        }
    };

    const handleAddColumn = async (title) => {
        const token = localStorage.getItem('jwtToken');
        if (!token) {
            alert('No estás autenticado.');
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/columns?title=${encodeURIComponent(title)}&boardId=${activeBoard.id}`, {
                method: 'POST',
                headers: {
                    'Authorization' : `Bearer ${token}`
                }
            });

            if(response.ok) {
                alert(`Columna "${title}" creada con éxito.`);
                setRefreshBoards(prev => !prev);
                setShowAddColumnModal(false);
            } else {
                const errorData = await response.json();
                alert(`Error al crear la columna: ${errorData.message}`);
            }
        } catch (error) {
            console.error(error);
            alert("Error de conexión al crear la columna");
        }
    };

    if (!user || !activeBoard) {
        return <div className="p-4">Cargando datos de usuario y tablero...</div>;
    }

    return (
        <div className="flex h-screen bg-gray-200">
            {/* 👈 Menú lateral izquierdo */}
            <div className="w-64 bg-white p-4 shadow-md flex-shrink-0 flex flex-col justify-start space-y-6">
                <h1 className="text-2xl font-bold text-gray-900 mb-8">Zenitho</h1>
                <div className="w-full space-y-2">
                    <button
                        onClick={() => setShowAddBoardModal(true)}
                        className="w-full p-2 bg-blue-500 hover:bg-blue-600 text-black font-bold rounded-md text-sm"
                    >
                        Añadir Tablero
                    </button>
                    <button
                        onClick={() => setShowAddCardModal(true)}
                        className="w-full p-2 bg-green-500 hover:bg-green-600 text-black font-bold rounded-md text-sm"
                    >
                        Añadir Tarjeta
                    </button>
                    <button
                        onClick={() => setShowAddColumnModal(true)}
                        className="w-full p-2 bg-yellow-500 hover:bg-yellow-600 text-black font-bold rounded-md text-sm"
                    >
                        Añadir Columna
                    </button>
                    <div className="mt-4 space-y-1">
                        {boards.map(board => (
                            <div
                                key={board.id}
                                onClick={() => setActiveBoard(board)}
                                className={`p-2 rounded-md cursor-pointer text-center ${activeBoard.id === board.id ? 'bg-gray-300 font-semibold' : 'bg-gray-100 hover:bg-gray-200'}`}
                            >
                                {board.title}
                            </div>
                        ))}
                    </div>
                </div>
            </div>

            {/* 👈 Contenido principal (barra superior y tablero) */}
            <div className="flex-1 flex flex-col">
                {/* 👈 Barra superior (Navbar) */}
                <div className="bg-white shadow-md p-4 flex justify-between items-center">
                    <span className="font-semibold text-gray-900">Bienvenido, {user.name}</span>

                    <button
                        onClick={handleLogoutSuccess}
                        className="ml-4 p-2 bg-red-500 hover:bg-red-600 text-black font-bold rounded-md"
                    >
                        Cerrar sesión
                    </button>
                </div>

                {/* 👈 Contenedor del tablero */}
                <div className="flex-1 overflow-auto p-4">
                    <KanbanBoard board={activeBoard} setBoard={setActiveBoard} key={refreshBoards} />
                </div>
            </div>

            {/* Modales */}
            <AddBoardModal
                isOpen={showAddBoardModal}
                onClose={() => setShowAddBoardModal(false)}
                onAddBoard={handleAddBoard}
            />

            <AddCardModal
                isOpen={showAddCardModal}
                onClose={() => setShowAddCardModal(false)}
                onAddCard={handleAddCard}
                columns={activeBoard.columns || []}
            />
            <AddColumnModal
                isOpen={showAddColumnModal}
                onClose={() => setShowAddColumnModal(false)}
                onAddColumn={handleAddColumn}
            />
        </div>
    );
};

export default Dashboard;