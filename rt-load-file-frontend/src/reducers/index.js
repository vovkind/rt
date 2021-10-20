const INITIAL_STATE = {
     parsedData: undefined
}

const reducer = (state = INITIAL_STATE, action) => {
     switch (action.type) {
        case 'SAVE_PARSED_DATA':
            return { ...state, parsedData: action.data };

          default:
               return state;
     }
};
export default reducer;