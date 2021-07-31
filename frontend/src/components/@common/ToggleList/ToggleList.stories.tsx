import React from 'react';

import Chip from '../Chip/Chip';
import ToggleList from './ToggleList';

export default {
  title: 'components/common/ToggleList',
  component: ToggleList,
  parameters: { actions: { argTypesRegex: '^on.*' } },
};

export const Default = () => (
  <ToggleList width="20rem" height="2.25rem">
    <Chip.Solid>안녕하세요</Chip.Solid>
    <Chip.Solid>안녕하세요</Chip.Solid>
    <Chip.Solid>안녕하세요</Chip.Solid>
    <Chip.Solid>안녕하세요</Chip.Solid>
    <Chip.Solid>안녕하세요</Chip.Solid>
  </ToggleList>
);
