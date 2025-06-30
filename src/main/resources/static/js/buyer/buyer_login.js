document.querySelector(".buyer-form").addEventListener("submit", function (e) {
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

  // API 호출
  fetch("/api/buyers/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({ email, password })
  })
    .then(res => {
      if (!res.ok) {
        throw new Error(`HTTP error! status: ${res.status}`);
      }
      return res.json();
    })
    .then(data => {
      console.log("서버 응답:", data); // 디버깅용

      // ApiResponse 구조에 맞게 수정
      if (data.success === true || data.errorCode === "0" || data.errorCode === 0) {
        alert("구매자 로그인 성공! 메인페이지로 이동합니다.");
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