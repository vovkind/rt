import { memo } from 'react';
import { useDrag, useDrop } from 'react-dnd';
import { ItemTypes } from './DNDItemTypes';


export const Row = memo(function Row({ id, data, moveRow, removeRow, findRow, }) {
    const originalIndex = findRow(id).index;

    //Drag row implementation
    const [{ isDragging }, drag] = useDrag(() => ({
        type: ItemTypes.ROW,
        item: { id, originalIndex },
        collect: (monitor) => ({
            isDragging: monitor.isDragging(),
        }),
        end: (item, monitor) => {
            const { id: droppedId, originalIndex } = item;
            const didDrop = monitor.didDrop();
            if (!didDrop) {
                removeRow(droppedId, originalIndex);
            }
        },
    }), [id, originalIndex, moveRow, removeRow]);

    //Drop row implementation
    const [, drop] = useDrop(() => ({
        accept: ItemTypes.ROW,
        canDrop: () => false,
        hover({ id: draggedId }) {
            if (draggedId !== id) {
                const { index: overIndex } = findRow(id);
                moveRow(draggedId, overIndex);
            }
        },
    }), [findRow, moveRow]);
    const opacity = isDragging ? 0 : 1;
    const getRowElements = (rowElement) => {
        let items = [];
        for (const rowKey in rowElement) {
            if(rowKey !== 'id'){
                items.push(rowElement[rowKey]);
            }
          }
        
        return items;
    }

    return (<tr ref={(node) => drag(drop(node))} style={{ opacity }}>
        { getRowElements(data).map( (col, index) =>{
            return <td key={index}>{col}</td>
        })}
		</tr>);
});
