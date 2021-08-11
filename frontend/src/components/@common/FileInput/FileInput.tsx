import React, { InputHTMLAttributes } from 'react';

import Styled from './FileInput.styles';

interface Props extends InputHTMLAttributes<HTMLInputElement> {
  fileName: string;
}

const FileInput = ({ fileName, ...options }: Props) => {
  return (
    <Styled.Root>
      <Styled.Label>
        <input type="file" {...options} />
        <span>파일 선택</span>
      </Styled.Label>
      <Styled.FileNameText>{fileName || '파일을 선택해주세요.'}</Styled.FileNameText>
    </Styled.Root>
  );
};

export default FileInput;
