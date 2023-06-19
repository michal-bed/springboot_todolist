import logo from './logo.svg';
import './App.css';
import Header from "./components/Header";
import TaskList from "./components/TaskList";
import axios from "axios";
import {useState} from "react";
export const REST_API_PORT = process.env.REACT_APP_REST_API_PORT ?? "8080";

function App() {
    const [todoTasks, setTodoTasks] = useState([])
    const fetchData = async () => {
        console.log(`http://localhost:${REST_API_PORT}/api/todo-item`);
        const res = await axios.get(`http://localhost:${REST_API_PORT}/api/todo-item`)
        if (res.status === 200) {
            console.log(res.data);
            const items = res.data;
            console.log("fetched");
            console.log(items);
            items.sort((a, b) => {
                let value = Date.parse(a.createdOn) - Date.parse(b.createdOn);
                console.log(Date.parse(a.createdOn).toLocaleString()
                    + " - " +  Date.parse(b.createdOn).toLocaleString()
                    + " = " + value)
                return value;
            });
            console.log("sorted");
            console.log(items);
            setTodoTasks(items ?? []);
        }
        else if (res.status === 204)
        {
            console.log("No content");
            setTodoTasks([]);
        }
        return res;
    }
  return (
    <div className="App">
      <Header fetchData={fetchData} setTodoTasks={setTodoTasks}/>
      <TaskList fetchData={fetchData} todoTasks={todoTasks} />
    </div>
  );
}

export default App;
