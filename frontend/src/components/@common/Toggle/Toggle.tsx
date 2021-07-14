import React from 'react';

import Styled from './Toggle.styles';

export interface Props {
  labelText?: string;
  checked?: boolean;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

const Toggle = ({ labelText = '', checked = false, onChange }: Props) => {
  return (
    <Styled.Label>
      <Styled.Text>{labelText}</Styled.Text>
      <Styled.Input type="checkbox" onChange={onChange} />
      <Styled.Toggle checked={checked}></Styled.Toggle>
    </Styled.Label>
  );
};

export default Toggle;
