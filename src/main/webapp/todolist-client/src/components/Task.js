import '../main.css';

import TaskItem from "./TaskItem";
import Actions from "./Actions";
import {useRef, useState} from "react";

export default function(props) {

    const [isReadOnly, setReadOnly] = useState(!props.task?.edited)
    const [taskValue, setTaskValue] = useState(props.task?.itemValue ?? "")
    const [toSave, setToSave] = useState(false)
    const taskRef = useRef(null);

    // useEffect(() =>
    // {
    //     setTaskValue(props.task?.todoItemValue)
    // }, [])

    function handleValueChange(e) {
        setTaskValue(e.target?.value);
    }

    function handleTaskClick() {

    }

    return (<div className="task">
        <TaskItem
            taskId={props.task?.todoItemId}
            isChecked={props.task?.checked}
            isReadOnly={isReadOnly}
            value={taskValue}
            taskRef={taskRef}
            setIfSave={setToSave}
            handleValueChange={handleValueChange}
        />
        <Actions taskId={props.task?.todoItemId}
                 isEdited={props.task?.edited}
                 setReadOnly={setReadOnly}
                 handleClick={handleTaskClick}
                 setTaskValue={setTaskValue}
                 taskRef={taskRef}
                 taskValue={taskValue}
                 ifSave={toSave}
                 setIfSave={setToSave}
                 fetchData={props.fetchData}
        />
        </div>);
}