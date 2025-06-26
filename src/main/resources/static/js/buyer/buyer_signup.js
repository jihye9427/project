// ✅ DOM 로딩 후 각 입력 필드 이벤트 바인딩
document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("name").addEventListener("input", validateName);
  document.getElementById("nickname").addEventListener("input", validateNickname);
  document.getElementById("email").addEventListener("input", validateEmail);
  document.getElementById("password").addEventListener("input", validatePassword);
  document.getElementById("password2").addEventListener("input", validatePasswordMatch);
  document.getElementById("birth").addEventListener("change", validateBirth);
  document.getElementById("address").addEventListener("input", validateAddress);

  const telInput = document.getElementById("tel");
  telInput.addEventListener("input", function () {
    const formatted = formatPhoneNumber(this.value);
    if (formatted.length <= 13) this.value = formatted;
    validateTel();
  });
});


// 🔢 전화번호 하이픈 자동 삽입
function formatPhoneNumber(value) {
  let numbers = value.replace(/\D/g, '').slice(0, 11); // 숫자만 추출 (11자리 제한)

  if (numbers.startsWith("02")) {
    if (numbers.length < 3) return numbers;
    if (numbers.length < 6) return numbers.slice(0, 2) + '-' + numbers.slice(2);
    return numbers.slice(0, 2) + '-' + numbers.slice(2, 6) + '-' + numbers.slice(6);
  } else {
    if (numbers.length < 4) return numbers;
    if (numbers.length < 8) return numbers.slice(0, 3) + '-' + numbers.slice(3);
    return numbers.slice(0, 3) + '-' + numbers.slice(3, 7) + '-' + numbers.slice(7);
  }
}


// -------------------------------------------
// ✅ 각 필드 유효성 검사 함수
// -------------------------------------------
// 이름
function validateName() {
  const name = document.getElementById("name").value.trim();
  const error = document.getElementById("error-name");
  const isKor = /^[가-힣]{2,8}$/.test(name);
  const isEng = /^[a-zA-Z\s]{2,45}$/.test(name);
  error.textContent = (!isKor && !isEng) ? "이름은 2~8자 입력해주세요." : "";
}
// 닉네임
function validateNickname() {
  const nickname = document.getElementById("nickname").value.trim();
  const error = document.getElementById("error-nickname");
  error.textContent = /^[가-힣a-zA-Z0-9]{2,8}$/.test(nickname)
    ? ""
    : "닉네임은 2~8자로 입력해주세요.";
}
// 이메일
function validateEmail() {
  const email = document.getElementById("email").value.trim();
  const error = document.getElementById("error-email");
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  error.textContent = (!emailRegex.test(email) || email.length > 50)
    ? "올바른 이메일 형식이여야 합니다."
    : "";
}
// 비밀번호
function validatePassword() {
  const pw = document.getElementById("password").value;
  const error = document.getElementById("error-password");
  const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z\d!@#$%^&*]{8,15}$/;
  error.textContent = pwRegex.test(pw)
    ? ""
    : "비밀번호는 8~15자, 영문+숫자+특수문자 포함해야 합니다.";
  validatePasswordMatch();
}
// 비밀번호 확인
function validatePasswordMatch() {
  const pw = document.getElementById("password").value;
  const pw2 = document.getElementById("password2").value;
  const error = document.getElementById("error-password2");
  error.textContent = (pw !== pw2) ? "비밀번호가 일치하지 않습니다." : "";
}
// 전화번호
function validateTel() {
  const tel = document.getElementById("tel").value.trim();
  const error = document.getElementById("error-tel");
  const telRegex = /^(01[016789]|02|0[3-9][0-9])-\d{3,4}-\d{4}$/;
  error.textContent = telRegex.test(tel)
    ? ""
    : "전화번호를 입력하여야 합니다.";
}
// 생년월일
function validateBirth() {
  const birth = document.getElementById("birth").value;
  const error = document.getElementById("error-birth");
  error.textContent = !birth ? "생년월일을 입력해주세요." : "";
  validatePassword();
  validatePasswordMatch();
  validateTel();
  validateBirth();
  validateAddress();
}
// 주소
function validateAddress() {
  const address = document.getElementById("address").value.trim();
  const error = document.getElementById("error-address");
  error.textContent = (address.length < 5 || address.length > 200)
    ? "주소를 입력해주세요."
    : "";
}

// ✅ 폼 제출 처리 - 서버로 전송 방식 (제거)
// 폼 제출은 서버사이드에서 처리하고, 클라이언트에서는 실시간 유효성 검사만 담당합니다.
// 아래 로직은 현재 HTML 구조와 맞지 않으므로 제거합니다.
