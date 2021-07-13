import React, { ChangeEvent, useState } from 'react';

import Styled from './FileInput.styles';

const FileInput = () => {
  const [file, setFile] = useState<File | null>(null);

  const onChange = (event: ChangeEvent<HTMLInputElement>) => {
    setFile(event.currentTarget.files[0]);
  };

  return (
    <Styled.RootContainer>
      <Styled.Label>
        <input type="file" onChange={onChange} />
        <span>파일 선택</span>
      </Styled.Label>
      <Styled.FileNameText>{file ? file.name : '파일을 선택해주세요.'}</Styled.FileNameText>
    </Styled.RootContainer>
  );
};

export default FileInput;
