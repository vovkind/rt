import React from 'react';
import axios from 'axios';
import store from '../Store';
import saveParsedFileData from '../actions';
import './UploadFile.css'


/**
 * UploadFile component
 * 
 * Loads provided file and pass its content to the server
 */
const UploadFile = () => {  
    // Handles file upload event and updates state
    function handleUpload(event) {
      const selectedFile = event.target.files[0];  
      const formData = new FormData();
    
      // Update the formData object
      formData.append("file", selectedFile);
      axios.post("/transformFile", formData, {
      }).then((response) => {
        store.dispatch(saveParsedFileData(response.data));
      });
    }

    return ( <div className="upload-file">
              <input type="file" onChange={handleUpload} />
            </div>
      );
}

export default UploadFile;