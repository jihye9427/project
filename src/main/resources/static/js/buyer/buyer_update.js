document.addEventListener('DOMContentLoaded', () => {
    const editBtn = document.getElementById('edit-btn');
    const saveBtn = document.getElementById('save-btn');
    const cancelBtn = document.getElementById('cancel-btn');
    const leaveBtn = document.getElementById('leave-btn');
    const passwordField = document.getElementById('password-field');

    const editableFields = {
        nickname: document.getElementById('nickname'),
        address: document.getElementById('address'),
        tel: document.getElementById('tel')
    };

    let initialFormState = {};

    // 수정 버튼 클릭 이벤트
    editBtn.addEventListener('click', () => {
        // 현재 폼 상태 저장
        for (const key in editableFields) {
            initialFormState[key] = editableFields[key].value;
        }

        // '수정 모드'로 전환
        for (const key in editableFields) {
            editableFields[key].readOnly = false;
        }
        
        // 버튼 상태 변경
        editBtn.style.display = 'none';
        leaveBtn.style.display = 'none';
        saveBtn.style.display = 'inline-block';
        cancelBtn.style.display = 'inline-block';
        
        // 비밀번호 필드 표시
        passwordField.style.display = 'flex'; // 또는 'block'
    });

    // 취소 버튼 클릭 이벤트
    cancelBtn.addEventListener('click', () => {
        // 페이지를 새로고침하여 원래 상태로 복원
        window.location.reload();
    });
    
    // 폼 제출 전 유효성 검사
    document.getElementById('buyer-update-form').addEventListener('submit', (e) => {
        // '수정 모드'일 때만 검사
        if (saveBtn.style.display === 'inline-block') {
            // 1. 변경 사항이 있는지 확인
            let isChanged = false;
            for (const key in editableFields) {
                if (initialFormState[key] !== editableFields[key].value) {
                    isChanged = true;
                    break;
                }
            }
            
            // 2. 현재 비밀번호가 입력되었는지 확인
            const currentPassword = document.getElementById('currentPassword').value;
            if (!currentPassword) {
                alert('정보를 수정하려면 현재 비밀번호를 입력해야 합니다.');
                e.preventDefault();
                return;
            }

            // 3. 변경 사항이 없으면 제출 막기
            if (!isChanged) {
                alert('변경된 내용이 없습니다.');
                e.preventDefault();
                return;
            }
        }
    });
}); 