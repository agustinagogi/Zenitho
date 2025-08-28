import React, { useState, useEffect } from 'react';
import CardEditor from './CardEditor';
import CardContent from './CardContent';

const KanbanBoard = () => {
    const [board, setBoard] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [editingCardId, setEditingCardId] = useState(null);

    useEffect(() => {
        const fetchBoardData = async () => {
            const token = localStorage.getItem('jwtToken'); // Obtén el token del almacenamiento local
            if (!token) {
                setError('No hay token de autenticación. Por favor, inicia sesión.');
                setLoading(false);
                return;
            }

            try {
                // Asumiendo que obtienes el primer tablero del usuario
                // En un proyecto más avanzado, tendrías un endpoint para obtener el tablero actual del usuario
                const response = await fetch('http://localhost:8080/api/boards', {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                    },
                });

                if (!response.ok) {
                    throw new Error('Error al obtener los datos del tablero.');
                }

                const boards = await response.json();
                // Asignamos el primer tablero que devuelva el backend
                if (boards && boards.length > 0) {
                    setBoard(boards[0]);
                } else {
                    setError('No se encontraron tableros.');
                }
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchBoardData();
    }, []); // La dependencia vacía asegura que se ejecuta solo una vez al montar

    const handleUpdateCardContent = async (cardId, newContent) => {
        const token = localStorage.getItem('jwtToken');
        if (!token) return;

        try {
            await fetch(`http://localhost:8080/api/cards/${cardId}/content`, {
                method: 'PATCH', // 👈 Usa el endpoint de PATCH
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(newContent),
            });
            console.log(`Contenido de la tarjeta ${cardId} actualizado.`);
            // Opcional: actualiza el estado localmente para reflejar el cambio
            setBoard(prevBoard => {
                const newBoard = { ...prevBoard };
                newBoard.columns = newBoard.columns.map(col => ({
                    ...col,
                    cards: col.cards.map(c => c.id === cardId ? { ...c, content: newContent } : c)
                }));
                return newBoard;
            });
        } catch (error) {
            console.error('Error al guardar el contenido:', error);
        }
    };


    if (loading) {
        return <div className="text-center mt-8">Cargando tablero...</div>;
    }

    if (error) {
        return <div className="text-center mt-8 text-red-500">Error: {error}</div>;
    }

    if (!board) {
        return <div className="text-center mt-8">No hay tablero para mostrar.</div>;
    }


    return (
        <div className="p-4 bg-gray-200 min-h-screen">
            <h1 className="text-3xl font-bold mb-6">{board.title}</h1>
            <div className="flex space-x-4 overflow-x-auto">
                {board.columns && board.columns.map(column => (
                    <div key={column.id} className="w-80 p-4 bg-gray-100 rounded-lg shadow-md flex-shrink-0">
                        <h2 className="text-xl font-semibold mb-4">{column.title}</h2>
                        {column.cards && column.cards.map(card => (
                            <div
                                key={card.id}
                                className="p-4 bg-white rounded-md shadow mb-4"
                                onClick={() => setEditingCardId(card.id)} // 👈 Al hacer clic, activa el modo edición
                            >
                                <h3>{card.title}</h3>
                                {editingCardId === card.id ? (
                                    <CardEditor card={card} onUpdate={handleUpdateCardContent} />
                                ) : (
                                    card.content && <CardContent content={card.content} />
                                )}
                            </div>
                        ))}
                    </div>
                ))}
            </div>
        </div>
    );
};

export default KanbanBoard;