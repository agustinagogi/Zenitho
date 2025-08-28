import React, { useState } from 'react';
import CardEditor from './CardEditor';
import CardContent from './CardContent';
import { DndContext, closestCenter, KeyboardSensor, PointerSensor, useSensor, useSensors } from '@dnd-kit/core';
import { arrayMove, SortableContext, horizontalListSortingStrategy } from '@dnd-kit/sortable';
import Column from './Column';

const KanbanBoard = ({ board, setBoard }) => {
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
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(newContent),
            });
            console.log(`Contenido de la tarjeta ${cardId} actualizado.`);
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

    const handleDragEnd = async (event) => {
        const { active, over } = event;
        if (!over || active.id === over.id) {
            return;
        }

        const originalColumns = [...board.columns]; // Guardar el estado original
        const sortedColumns = [...originalColumns].sort((a, b) => a.position - b.position);

        const oldIndex = sortedColumns.findIndex((c) => c.id === active.id);
        const newIndex = sortedColumns.findIndex((c) => c.id === over.id);

        const reorderedColumns = arrayMove(sortedColumns, oldIndex, newIndex);

        // Actualización optimista de la UI
        setBoard(prevBoard => ({
            ...prevBoard,
            columns: reorderedColumns.map((col, index) => ({ ...col, position: index })),
        }));

        const token = localStorage.getItem('jwtToken');
        if (!token) {
            setBoard(prevBoard => ({ ...prevBoard, columns: originalColumns }));
            return;
        }

        const updatePromises = reorderedColumns.map((column, index) =>
            fetch(`http://localhost:8080/api/columns/${column.id}/position?position=${index}`, {
                method: 'PATCH',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            })
        );

        try {
            const responses = await Promise.all(updatePromises);

            if (responses.some(res => !res.ok)) {
                throw new Error('Una o más actualizaciones de posición de columna fallaron.');
            }

            console.log('Todas las posiciones de las columnas se actualizaron correctamente.');

        } catch (error) {
            console.error('Error al actualizar la posición de las columnas:', error);
            // Si algo falla, revertir la UI al estado original
            setBoard(prevBoard => ({ ...prevBoard, columns: originalColumns }));
            alert('No se pudo guardar el nuevo orden de las columnas. Inténtalo de nuevo.');
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