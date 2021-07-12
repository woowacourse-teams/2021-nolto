import React from 'react';

import LevelLinkButton, { Props } from './LevelLinkButton';

export default {
  title: 'components/LevelLinkButton',
  component: LevelLinkButton,
  argTypes: {
    selected: {
      control: { type: 'boolean' },
    },
  },
};

export const Progress = ({ selected }: Props) => <LevelLinkButton.Progress selected={selected} />;

export const Complete = ({ selected }: Props) => <LevelLinkButton.Complete selected={selected} />;

export const SOS = ({ selected }: Props) => <LevelLinkButton.SOS selected={selected} />;
