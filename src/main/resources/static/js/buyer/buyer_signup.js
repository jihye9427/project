// ✅ Web Controller용 수정된 버전
document.addEventListener("DOMContentLoaded", function() {
  const form = document.getElementById("buyer-signup-form") ||
               document.querySelector(".buyer-form") ||
               document.querySelector("form");

  if (!form) {
    console.error("폼 요소를 찾을 수 없습니다!");
    return;
  }

  form.addEventListener("submit", function (e) {
    e.preventDefault();

    // 유효성 검사 생략...

    // FormData 사용 (Web Controller용)
    const formData = new FormData();
    formData.append('name', document.getElementById("name").value.trim());
    formData.append('nickname', document.getElementById("nickname").value.trim());
    formData.append('email', document.getElementById("email").value.trim());
    formData.append('password', document.getElementById("password").value);
    formData.append('tel', document.getElementById("tel").value.trim());
    formData.append('gender', document.getElementById("gender").value);
    formData.append('birth', document.getElementById("birth").value);
    formData.append('address', document.getElementById("address").value.trim());

    // Web Controller 호출
    fetch("/buyer/signup", {
      method: "POST",
      body: formData  // JSON이 아닌 FormData 사용
    })
      .then(res => {
        if (res.redirected) {
          window.location.href = res.url;
        } else if (res.ok) {
          alert("회원가입이 완료되었습니다!");
          window.location.href = "/buyer/login";
        } else {
          return res.text().then(msg => { throw new Error(msg); });
        }
      })
      .catch(err => {
        console.error('회원가입 처리 중 오류 발생:', err);
        alert("회원가입 처리 중 오류가 발생했습니다.");
      });
  });
});