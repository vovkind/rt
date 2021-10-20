import { memo, useCallback, useState } from 'react';
import { useDrop } from 'react-dnd';
import { Row } from './Row';
import update from 'immutability-helper';
import { ItemTypes } from './DNDItemTypes';
import './DNDTable.css';

const DNDTable = memo(function Container(props) {
    const allRecords = props.data;
    const [rows, setRows] = useState(allRecords);
    const isNegativeAmount = (row) => row.sign === '-';
    
    //Calc total amount of all rows  
    const calcTotalAmount = (allRecords) => {
        let total = 0;

        allRecords.forEach((record) => {
            total += isNegativeAmount(record) ? (-1 * record['amount']) : record['amount']; 
        })

        return total;
    }
    const [amount, setAmount] = useState(calcTotalAmount(allRecords));

    // Find selected row
    const findRow = useCallback((id) => {
        const row = rows.filter((c) => `${c.id}` === id)[0];
        return {
            row,
            index: rows.indexOf(row),
        };
    }, [rows]);

    // Actually move row to another position
    const moveRow = useCallback((id, atIndex) => {
        const { row, index } = findRow(id);
        setRows(update(rows, {
            $splice: [
                [index, 1],
                [atIndex, 0, row],
            ],
        }));
    }, [findRow, rows, setRows]);

    // Remove row upon dran and drop out of the table
    const removeRow = useCallback((id, atIndex) => {
        const { row, index } = findRow(id);
        setRows(update(rows, {
            $splice: [[index, 1]],
        }));
        setAmount(isNegativeAmount(row) ? (amount+row.amount) : (amount-row.amount));
    }, [findRow, rows, setRows, amount, setAmount]);
    const [, drop] = useDrop(() => ({ accept: ItemTypes.ROW }));

    return (<div>
                      <div className="dnd-instr">
                * Drag&Drop to move the record to another row <br />
                * Drag&Drop outside of the table to remove the record
              </div>
                <div className="total-amount">Total amount:{amount}</div>
                <div className="tableFixHead">
                    <table ref={drop}>
                        <thead>
                            <tr>
                                <th>DocType</th>
                                <th>CompanyID</th>
                                <th>Date</th>
                                <th>DocID</th>
                                <th>Sign</th>
                                <th>Amount</th>
                            </tr>
                            </thead>
                        <tbody>
                            {rows.map((row) => (<Row 
                                                    key={row.id} 
                                                    id={`${row.id}`} 
                                                    data={row} 
                                                    moveRow={moveRow} 
                                                    removeRow={removeRow} 
                                                    findRow={findRow}/>))}
                        </tbody>
                    </table>
                </div>
            </div>);
});

export default DNDTable;
