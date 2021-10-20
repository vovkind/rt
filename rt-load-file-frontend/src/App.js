import './App.css';
import UploadFile from './components/UploadFile';
import DNDTable from './components/DNDTable'
import { DndProvider } from 'react-dnd'
import { HTML5Backend } from 'react-dnd-html5-backend';
import { useSelector } from "react-redux";


function App() {
  const parsedData = useSelector(state => state.parsedData);
  return (
    <div className="App">
        <main className='app-content'>
          <UploadFile />
          {parsedData ?
            <DndProvider backend={HTML5Backend}>
              <DNDTable data={parsedData} />
            </DndProvider>
            :
            null
          }
          </main>
    </div>
  );
}

export default App;