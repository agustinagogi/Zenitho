import React from 'react';
import { useEditor, EditorContent } from '@tiptap/react';
import StarterKit from '@tiptap/starter-kit';

const CardContent = ({content}) => {
    const editor = useEditor({
        extensions: [
            StarterKit,
        ],
        content: JSON.parse(content || '{}'),
        editable: false,
    });

    if(!editor) return null;

    return (
        <div className="prose max-w-none">
            <EditorContent editor={editor} />
        </div>
    );
};

export default CardContent;