import React from 'react';
import {useSortable} from "@dnd-kit/sortable";
import {CSS} from '@dnd-kit/utilities';
import CardEditor from './CardEditor';
import CardContent from './CardContent';

const Column = ({column, editingCardId,setEditingCardId, handleUpdateCardContent}) => {
    const {
        attributes,
        listeners,
        setNodeRef,
        transform,
        transition
    } = useSortable({id: column.id});

    const style = {
        transform: CSS.Transform.toString(transform),
        transition,
    };

    return (
        <div
            ref={setNodeRef}
            style={style}
            className="w-80 p-4 bg-gray-100 rounded-lg shadow-md flex-shrink-0"
        >
            <div {...attributes} {...listeners} className="cursor-grab">
                <h2 className="text-xl font-semibold mb-4">{column.title}</h2>
            </div>
            {/* Render cards inside the column */}
            {column.cards && column.cards.map(card => (
                <div
                    key={card.id}
                    className="p-4 bg-white rounded-md shadow mb-4"
                    onClick={() => setEditingCardId(card.id)}
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
    );
};

export default Column;