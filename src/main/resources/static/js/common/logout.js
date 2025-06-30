function logout() {
  // 🔁 1. 서버에 로그아웃 요청
  fetch("/logout", {
    method: "POST",
    headers: {
      "X-Requested-With": "XMLHttpRequest"
    }
  })
    .then(response => {
      if(response.ok) {
        window.location.href = "/";
      } else {
        throw new Error('로그아웃 실패');
      }
    })
    .catch(err => {
      alert("로그아웃 실패: " + err.message);
    });
}
