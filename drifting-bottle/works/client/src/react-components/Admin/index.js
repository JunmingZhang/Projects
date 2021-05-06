import React from "react"

import "./style.css"
import Title from "./../Title";
import { uid } from 'react-uid'
import Button from "@material-ui/core/Button";
import { getUsers, delUser } from "../../actions/admin";

class Admin extends React.Component {
    state = {
        bgcolor: this.props.appState.bgcolor,
        userEmail: "", // User name is register email.
        userPassword: "",

        // we should get all the users from database 
        users: [
        ]
    }

    handleInputChange = (event) => {
        const target = event.target
        const value = target.value
        const name = target.name
        this.setState({[name]: value})
    }

    removeUser = () => {
        // we should delete this user from database
        const toMove = this.state.userEmail
        const fileterUser = this.state.users.filter((u) => {
            return (u.email !== toMove)
        })
        this.setState({
            users: fileterUser
        })
        delUser(toMove);
    }

    deleteUser = (user) => {
        // we should delete this user from database
        const deletedUserList = this.state.users.filter((u) => {
            return u !== user
        })
        this.setState({
            users: deletedUserList
        })
        console.log("user.userEmail", user)
        delUser(user.email);
    }
    
    render() {
        // console.log("hhh")
        // console.log("this", this)
        // getUsers(this);
        return (
        <div className="page" style={{background: this.state.bgcolor}}>
            <div className="App">
                <Title
                title={`Administrator`}
                subtitle={`Below are the users and their passwords`}
                />
                <Button
                variant="contained"
                color="primary"
                className="user-form__submit-button"
                onClick={() => getUsers(this)}
              >
                Show Users
              </Button>
                <input 
                    value={this.state.userEmail}
                    onChange = { this.handleInputChange } 
                    type="text" name="userEmail" placeholder="Email">
                
                </input>
                <input type="submit" 
                    value="Delete User"
                    onClick={this.removeUser}
                >               
                </input>
                <ul>
                    { this.state.users.map((user) => {
                        return (
                            <li key={uid(user)}>
                                User email: {user.email}
                                <button
                                    onClick={ () => this.deleteUser(user) }
                                >Delete</button>
                            </li>
                        )
                    }) }
                </ul>

            </div>       
        </div>
        );
    }
}

export default Admin;