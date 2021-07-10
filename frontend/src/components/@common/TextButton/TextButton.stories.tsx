import React from 'react';

import { ButtonStyle } from 'types';
import TextButton from './TextButton';

export default {
  title: 'components/common/TextButton',
  component: TextButton,
  argTypes: {},
};

export const RegularSolid = () => (
  <TextButton.Regular buttonStyle={ButtonStyle.SOLID}>엄청나게긴버튼</TextButton.Regular>
);

export const RoundedSolid = () => (
  <TextButton.Rounded buttonStyle={ButtonStyle.SOLID}>엄청나게긴버튼</TextButton.Rounded>
);

export const RegularOutline = () => (
  <TextButton.Regular buttonStyle={ButtonStyle.OUTLINE}>엄청나게긴버튼</TextButton.Regular>
);

export const RoundedOutline = () => (
  <TextButton.Rounded buttonStyle={ButtonStyle.OUTLINE}>엄청나게긴버튼</TextButton.Rounded>
);

export const RegularSolidReverse = () => (
  <TextButton.Regular buttonStyle={ButtonStyle.SOLID} reverse={true}>
    엄청나게긴버튼
  </TextButton.Regular>
);

export const RoundedSolidReverse = () => (
  <TextButton.Rounded buttonStyle={ButtonStyle.SOLID} reverse={true}>
    엄청나게긴버튼
  </TextButton.Rounded>
);
