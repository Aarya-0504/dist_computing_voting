import './App.css'
import HomePage from './pages/HomePage/HomePage'
import { BrowserRouter, Routes, Route } from "react-router-dom";


function App() {

  return (
    <>
    <BrowserRouter>
    <Routes>
        <Route path="/"  element={<HomePage />}/> 
        <Route path="/vote"  element={<HomePage />}/> 
    </Routes>
    </BrowserRouter>
    </>
  )
}

export default App
