import React from 'react';

import Styled from './LevelLinkButton.styles';

export interface Props {
  path?: string;
  selected?: boolean;
}

const Progress = ({ path = '/', selected = false }: Props) => {
  return (
    <Styled.Root to={path}>
      <Styled.Progress selected={selected}>🧩</Styled.Progress>
      <Styled.Text>조립중</Styled.Text>
    </Styled.Root>
  );
};

const Complete = ({ path = '/', selected = false }: Props) => {
  return (
    <Styled.Root to={path}>
      <Styled.Complete selected={selected}>🦄</Styled.Complete>
      <Styled.Text>전시중</Styled.Text>
    </Styled.Root>
  );
};

const SOS = ({ path = '/', selected = false }: Props) => {
  return (
    <Styled.Root to={path}>
      <Styled.SOS selected={selected}>🚨</Styled.SOS>
      <Styled.Text>SOS</Styled.Text>
    </Styled.Root>
  );
};

const LevelLinkButton = {
  Progress,
  Complete,
  SOS,
};

export default LevelLinkButton;
