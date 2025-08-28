import React, { useEffect, useState } from 'react';
import { useEditor, EditorContent } from '@tiptap/react';
import StarterKit from '@tiptap/starter-kit';

const CardEditor = ({ card, onUpdate }) => {
    const [content, setContent] = useState(card.content);

    const editor = useEditor({
        extensions: [
            StarterKit,
        ],
        content: JSON.parse(content || '{}'),
        onUpdate: ({ editor }) => {
            // Cuando el contenido del editor cambia, actualiza el estado local
            const updatedContent = JSON.stringify(editor.getJSON());
            setContent(updatedContent);
            onUpdate(card.id, updatedContent);
        },
    });

    return (
        <div className="border border-gray-300 rounded p-2">
            <EditorContent editor={editor} />
        </div>
    );
};

export default CardEditor;