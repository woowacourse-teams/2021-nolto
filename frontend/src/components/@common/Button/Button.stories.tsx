import React from 'react';

import { ButtonStyle } from 'types';
import Button from './Button';

export default {
  title: 'components/common/Button',
  component: Button,
  argTypes: {},
};

export const RegularSolid = () => (
  <Button.Regular buttonStyle={ButtonStyle.SOLID}>엄청나게긴버튼</Button.Regular>
);

export const RoundedSolid = () => (
  <Button.Rounded buttonStyle={ButtonStyle.SOLID}>엄청나게긴버튼</Button.Rounded>
);

export const RegularOutline = () => (
  <Button.Regular buttonStyle={ButtonStyle.OUTLINE}>엄청나게긴버튼</Button.Regular>
);

export const RoundedOutline = () => (
  <Button.Rounded buttonStyle={ButtonStyle.OUTLINE}>엄청나게긴버튼</Button.Rounded>
);

export const RegularSolidReverse = () => (
  <Button.Regular buttonStyle={ButtonStyle.SOLID} reverse={true}>
    엄청나게긴버튼
  </Button.Regular>
);

export const RoundedSolidReverse = () => (
  <Button.Rounded buttonStyle={ButtonStyle.SOLID} reverse={true}>
    엄청나게긴버튼
  </Button.Rounded>
);
