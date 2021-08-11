import React, { ButtonHTMLAttributes } from 'react';

import Styled from './LevelButton.styles';

export interface Props extends ButtonHTMLAttributes<HTMLButtonElement> {
  selected?: boolean;
}

const Progress = ({ selected = false, ...options }: Props) => {
  return (
    <Styled.Root {...options}>
      <Styled.Progress selected={selected}>🧩</Styled.Progress>
      <Styled.Text>조립중</Styled.Text>
    </Styled.Root>
  );
};

const Complete = ({ selected = false, ...options }: Props) => {
  return (
    <Styled.Root {...options}>
      <Styled.Complete selected={selected}>🦄</Styled.Complete>
      <Styled.Text>전시중</Styled.Text>
    </Styled.Root>
  );
};

const SOS = ({ selected = false, ...options }: Props) => {
  return (
    <Styled.Root {...options}>
      <Styled.SOS selected={selected}>🚨</Styled.SOS>
      <Styled.Text>SOS</Styled.Text>
    </Styled.Root>
  );
};

const LevelButton = {
  Progress,
  Complete,
  SOS,
};

export default LevelButton;
