import { useState } from "react"
import { Button, Container, Nav, Navbar } from "react-bootstrap"
import { Link } from "react-router-dom"

const Header = () => {
  //로그인 상태 관리
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const onLogout = () => {
    setIsLoggedIn(!isLoggedIn)
    window.localStorage.clear()
  }
  return (
    <>
      <Navbar expand="lg" className="bg-body-tertiary">
        <Container>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Link to="/" className="nav-link">Home</Link>
              <Link to="/login" className="nav-link">로그인</Link>
              <Link to="/board" className="nav-link">게시판</Link>
            </Nav>
            {isLoggedIn && <Button className="btn btn-danger" onClick={onLogout}>로그아웃</Button>}
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </>
  )
}

export default Header