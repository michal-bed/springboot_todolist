import '../main.css';
import {useEffect, useLayoutEffect, useState} from "react";
import {Checkbox, Fade, Tooltip, Zoom} from "@mui/material";
import axios from "axios";
import {REST_API_PORT} from "../App";

export default function TaskItem(props) {
    const [isChecked, setIsChecked] = useState(props?.isChecked ?? false);

    function setCaretPosition(ctrl, pos) {
        // Modern browsers
        if (ctrl.setSelectionRange) {
            ctrl.focus();
            ctrl.setSelectionRange(pos, pos);
            ctrl.blur();

            // IE8 and below
        } else if (ctrl.createTextRange) {
            var range = ctrl.createTextRange();
            range.collapse(true);
            range.moveEnd('character', pos);
            range.moveStart('character', pos);
            range.select();
        }
    }

    function scrollToInputEnd() {
        console.log("scrollWidth", props.taskRef.current.scrollWidth);
        console.log("clientWidth", props.taskRef.current.clientWidth);
        console.log("scrollLeft", props.taskRef.current.scrollLeft);

        const scrollLeft = props.taskRef.current.scrollWidth - props.taskRef.current.clientWidth;
        props.taskRef.current.scrollLeft = scrollLeft;
    }

    useEffect(() => {
        if(!props.taskRef?.current?.readOnly && document.activeElement === props.taskRef?.current) {
            console.log("Active element")
            scrollToInputEnd();
        }
        else {
            setCaretPosition(props.taskRef?.current, 0)
        }});

        useEffect(() => {
        const updateIsChecked = async () => {
            const res = await axios.patch(`http://localhost:${REST_API_PORT}/api/todo-item/is-checked`,
                { id: props.taskId, checked: isChecked }
            );
            if (res.status === 201) {
                console.log("Change checked");
            } else {
                alert("Some problem occurred.")
            }
        }
        updateIsChecked().catch();
    }, [isChecked])

    useEffect(() => {
        const listener = event => {
            if (event.code === "Enter" || event.code === "NumpadEnter") {
                if(document.activeElement === props.taskRef.current)
                {
                    console.log("Saving started");
                    props.setIfSave(true);
                    props.taskRef?.current?.blur();
                }
            }
        };
        document.addEventListener("keydown", listener);
        return () => {
            document.removeEventListener("keydown", listener);
        };
    }, []);

    // useEffect(() => {
    //         if (!props.isReadOnly)
    //         {
    //             props.taskRef?.current?.focus();
    //         }
    //     }, [props.isReadOnly]);

    return (<div className="content">
        <Checkbox
            className="checkbox"
            inputProps={{ 'aria-label': 'controlled' }}
            checked={isChecked}
            onClick={(event) => setIsChecked(prev => !prev)}
            style ={{
                color: "#ffffff",
            }}
        />
        <Tooltip TransitionComponent={Zoom}
                 TransitionProps={{ timeout: 600 }}
                 // title={props.value}
                 placement="top"
                 title={props.value != null && props.value !== "" && <div style={{ fontSize: "1rem" }}>{props.value}</div>}>
            <input
                data-totoditem-id={props.taskId}
                id={`task-input-${props.taskId}`}
                type="text"
                className={`text${isChecked ? ' crossed' : ''}`}
                value={props.value}
                onClick={props.handleClick}
                readOnly={props.isReadOnly}
                onChange={props.handleValueChange}
                ref={props.taskRef}
                style = {{overflowX: 'scroll'}}
                onFocus={(e)=>e.currentTarget.setSelectionRange(e.currentTarget.value.length, e.currentTarget.value.length)}
            />
        </Tooltip>
    </div>);
}