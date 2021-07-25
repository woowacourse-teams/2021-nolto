import styled from 'styled-components';

import Avatar from 'components/@common/Avatar/Avatar';

const Root = styled.ul`
  display: flex;
  flex-direction: column;
  gap: 2rem;
  align-items: center;
  height: 40rem;
  padding: 0 1rem;
  overflow: auto;

  &::-webkit-scrollbar {
    display: none;
  }

  /* Hide scrollbar for IE, Edge and Firefox */
  -ms-overflow-style: none;
  scrollbar-width: none;
`;

const VerticalAvatar = styled(Avatar)`
  margin-bottom: 12px;
`;

export default {
  Root,
  VerticalAvatar,
};
