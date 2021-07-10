import React from 'react';

import Styled from './Checkbox.styles';

export interface Props {
  labelText?: string;
  checked?: boolean;
  onChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
}

const Checkbox = ({ labelText = '', checked = false, onChange }: Props) => {
  return (
    <Styled.Label>
      <Styled.Text>{labelText}</Styled.Text>
      <Styled.Checkbox type="checkbox" checked={checked} onChange={onChange} />
      <Styled.CheckMark />
    </Styled.Label>
  );
};

export default Checkbox;
