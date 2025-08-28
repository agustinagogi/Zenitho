import React, { useState} from 'react';
import CardEditor from './CardEditor';
import CardContent from './CardContent';
import {DndContext, closestCenter, KeyboardSensor, PointerSensor, useSensor, useSensors} from '@dnd-kit/core';
import {arrayMove, SortableContext, horizontalListSortingStrategy,} from '@dnd-kit/sortable';
import Column from './Column';

const KanbanBoard = ({board, setBoard}) => {
    const [editingCardId, setEditingCardId] = useState(null);
    const sensors = useSensors(
        useSensor(PointerSensor),
        useSensor(KeyboardSensor),
    );

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

    const handleDragEnd = (event) =>{
        const {active, over} = event;
        if (active.id !== over.id){
            const oldIndex = board.columns.findIndex((c) => c.id === active.id);
            const newIndex = board.columns.findIndex((c) => c.id === over.id);

            const newColumns = arrayMove(board.columns, oldIndex, newIndex);

            setBoard(prevBoard => ({ ...prevBoard, columns: newColumns }));

            const token = localStorage.getItem('jwtToken');
            if (!token) return;

            newColumns.forEach(async (column, index) => {
                try {
                    await fetch(`http://localhost:8080/api/columns/${column.id}/position?position=${index}`, {
                        method: 'PATCH',
                        headers: {
                            'Authorization': `Bearer ${token}`,
                        },
                    });
                } catch (error) {
                    console.error('Error al actualizar la posición de la columna:', error);
                    // Opcional: revertir el estado si la API falla
                    setBoard(board);
                }
            });
        }
    };

    if (!board) {
        return <div className="text-center mt-8">No hay tablero para mostrar.</div>;
    }

    const sortedColumns = board.columns ? [...board.columns].sort((a, b) => a.position - b.position) : [];


    return (
        <DndContext
            sensors={sensors}
            collisionDetection={closestCenter}
            onDragEnd={handleDragEnd}
        >
            <SortableContext
                items={sortedColumns.map(c => c.id)}
                strategy={horizontalListSortingStrategy}
            >
                <div className="p-4 bg-gray-200 min-h-screen flex space-x-4 overflow-x-auto">
                    {sortedColumns.map(column => (
                        <Column
                            key={column.id}
                            column={column}
                            editingCardId={editingCardId}
                            setEditingCardId={setEditingCardId}
                            handleUpdateCardContent={handleUpdateCardContent}
                        />
                    ))}
                </div>
            </SortableContext>
        </DndContext>
    );
};

export default KanbanBoard;