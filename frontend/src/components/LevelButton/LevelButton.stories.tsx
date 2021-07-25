import React from 'react';

import LevelButton, { Props } from './LevelButton';

export default {
  title: 'components/LevelButton',
  component: LevelButton,
  argTypes: {
    selected: {
      control: { type: 'boolean' },
    },
  },
};

export const Progress = ({ selected }: Props) => <LevelButton.Progress selected={selected} />;

export const Complete = ({ selected }: Props) => <LevelButton.Complete selected={selected} />;

export const SOS = ({ selected }: Props) => <LevelButton.SOS selected={selected} />;
