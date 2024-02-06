const mongoose = require('mongoose');

const url = `mongodb+srv://aarya2:mOOCmkSZZpXB8neQ@cluster0.4ekkflm.mongodb.net/?retryWrites=true&w=majority`;

mongoose.connect(url, {
    useNewUrlParser: true, 
    useUnifiedTopology: true
}).then(() => console.log('Connected to DB')).catch((e)=> console.log('Error', e))