import React, { useState } from 'react';

const AddCardModal = ({ isOpen, onClose, onAddCard, columns }) => {
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [selectedColumnId, setSelectedColumnId] = useState(columns && columns.length > 0 ? columns[0].id : '');

    if (!isOpen) return null;

    const handleSubmit = async (e) => {
        e.preventDefault();
        await onAddCard(title, content, selectedColumnId);
        setTitle('');
        setContent('');
        setSelectedColumnId(columns && columns.length > 0 ? columns[0].id : '');
    };

    return (
        <div className="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full flex justify-center items-center z-50">
            <div className="relative p-5 border w-96 shadow-lg rounded-md bg-white">
                <h3 className="text-lg font-bold text-gray-900 mb-4">Añadir Nueva Tarjeta</h3>
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        placeholder="Título de la tarjeta"
                        className="w-full p-2 mb-4 border border-gray-300 rounded text-gray-900"
                        required
                    />
                    <textarea
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        placeholder="Contenido de la tarjeta"
                        className="w-full p-2 mb-4 border border-gray-300 rounded text-gray-900"
                        rows="4"
                    />
                    <label htmlFor="column-select" className="block mb-2 text-sm font-medium text-gray-700">
                        Selecciona la columna:
                    </label>
                    <select
                        id="column-select"
                        value={selectedColumnId}
                        onChange={(e) => setSelectedColumnId(e.target.value)}
                        className="w-full p-2 mb-4 border border-gray-300 rounded text-gray-900"
                        required
                    >
                        {columns.map(column => (
                            <option key={column.id} value={column.id}>
                                {column.title}
                            </option>
                        ))}
                    </select>

                    <div className="flex justify-end space-x-2">
                        <button
                            type="button"
                            onClick={onClose}
                            className="px-4 py-2 text-sm font-medium text-gray-700 bg-gray-200 rounded-md hover:bg-gray-300"
                        >
                            Cancelar
                        </button>
                        <button
                            type="submit"
                            className="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-md hover:bg-blue-700"
                        >
                            Guardar
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default AddCardModal;