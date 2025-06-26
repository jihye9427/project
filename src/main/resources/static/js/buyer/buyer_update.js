document.addEventListener('DOMContentLoaded', () => {
    const editBtn = document.getElementById('edit-btn');
    const saveBtn = document.getElementById('save-btn');
    const cancelBtn = document.getElementById('cancel-btn');
    const passwordField = document.getElementById('password-field');

    const editableFields = [
        document.getElementById('nickname'),
        document.getElementById('address'),
        document.getElementById('tel')
    ];

    // 수정 버튼 클릭 이벤트
    editBtn.addEventListener('click', () => {
        // '수정 모드'로 전환
        editableFields.forEach(field => field.readOnly = false);
        
        // 버튼 상태 변경
        editBtn.style.display = 'none';
        saveBtn.style.display = 'inline-block';
        cancelBtn.style.display = 'inline-block';
        
        // 비밀번호 필드 표시
        passwordField.style.display = 'flex'; // 또는 'block'
    });

    // 취소 버튼 클릭 이벤트
    cancelBtn.addEventListener('click', () => {
        // '조회 모드'로 되돌리기 (페이지 새로고침)
        window.location.reload();
    });
    
    // 폼 제출 전 유효성 검사 (선택사항, 현재는 비밀번호 입력 여부만 체크)
    document.getElementById('buyer-update-form').addEventListener('submit', (e) => {
        // '수정 모드'일 때만 검사
        if (saveBtn.style.display === 'inline-block') {
            const currentPassword = document.getElementById('currentPassword').value;
            if (!currentPassword) {
                alert('정보를 수정하려면 현재 비밀번호를 입력해야 합니다.');
                e.preventDefault(); // 폼 제출 중단
            }
        }
    });
}); 