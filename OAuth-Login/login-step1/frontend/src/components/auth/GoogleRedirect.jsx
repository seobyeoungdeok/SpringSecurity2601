import React from 'react'

const GoogleRedirect = () => {
  // 구글에서 보내주는 인가코드 받기
  const code = new URL(window.location.href).searchParams.get('code')
  console.log(code)
  return (
    <>
      loading ...
    </>
  )
}

export default GoogleRedirect
