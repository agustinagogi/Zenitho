import React, { useState} from 'react';
import CardEditor from './CardEditor';
import CardContent from './CardContent';

const KanbanBoard = ({board}) => {
    const [editingCardId, setEditingCardId] = useState(null);

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