function logout() {
  // 🔁 1. 서버에 로그아웃 요청
  fetch("/logout", {
    method: "POST",
    headers: {
      "X-Requested-With": "XMLHttpRequest"
    }
  })
    .then(res => {
      if (res.redirected) {
        window.location.href = res.url;
      } else {
        alert("로그아웃 되었습니다.");
        window.location.href = "/home";
      }
    })
    .catch(err => {
      alert("로그아웃 실패: " + err.message);
    });
}
