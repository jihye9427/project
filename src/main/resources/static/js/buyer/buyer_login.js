document.querySelector(".buyer-form").addEventListener("submit", async function (e) {
  e.preventDefault(); // 폼 기본 동작 방지

  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value;

  // 1. 유효성 검사
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

  // 2. 서버에 로그인 요청
  try {
    const formData = new URLSearchParams();
    formData.append('email', email);
    formData.append('password', password);

    const response = await fetch('/buyer/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: formData
    });

    if (response.ok && response.redirected) {
      window.location.href = response.url;
    } else {
      alert("이메일 또는 비밀번호가 일치하지 않습니다.");
      // 로그인 실패 시 현재 페이지에 머무르거나 로그인 페이지로 다시 이동
      // window.location.href = '/buyer/login';
    }
  } catch (err) {
    alert("로그인 처리 중 오류가 발생했습니다.");
    console.error(err);
  }
});
