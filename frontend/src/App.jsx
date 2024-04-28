import './App.css'
import HomePage from './pages/HomePage/HomePage'
import { BrowserRouter, Routes, Route } from "react-router-dom";
import VotePage from './pages/VotePage/VotePage';

function App() {

  return (
    <>
    <BrowserRouter>
    <Routes>
        <Route path="/"  element={<HomePage />}/> 
        <Route path="/vote"  element={<VotePage />}/> 
    </Routes>
    </BrowserRouter>
    </>
  )
}

export default App
