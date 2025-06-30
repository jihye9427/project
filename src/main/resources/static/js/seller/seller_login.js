document.querySelector(".seller-form").addEventListener("submit", function (e) {
  e.preventDefault();

  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value;

  // 유효성 검사
  if (!email || !password) {
    alert("이메일과 비밀번호를 모두 입력하세요.");
    return;
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email)) {
    alert("올바른 이메일 형식이 아닙니다.");
    return;
  }

  if (password.length < 4) {
    alert("비밀번호는 최소 4자 이상이어야 합니다.");
    return;
  }

  // 판매자 API 호출
  fetch("/api/sellers/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ email, password })
  })
    .then(res => {
      console.log("응답 상태:", res.status);
      if (!res.ok) {
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      return res.json();
    })
    .then(data => {
      console.log("서버 응답:", data);

      if (data.success === true && data.errorCode === 0) {
        alert("판매자 로그인 성공! 메인페이지로 이동합니다.");
        window.location.href = "/";
      } else {
        alert("로그인 실패: " + (data.message || "알 수 없는 오류"));
      }
    })
    .catch(err => {
      console.error("로그인 오류:", err);
      alert("로그인 처리 중 오류가 발생했습니다.");
    });
});